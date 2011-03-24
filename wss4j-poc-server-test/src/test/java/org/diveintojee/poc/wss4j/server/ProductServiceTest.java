package org.diveintojee.poc.wss4j.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.diveintojee.poc.wss4j.domain.Product;
import org.diveintojee.poc.wss4j.domain.services.ProductService;
import org.junit.Before;
import org.junit.Test;


/**
 * @author louis.gueye@gmail.com
 */
public class ProductServiceTest {

	private static final String WSS4J_POC_SERVER_URL = ResourceBundle.getBundle(Constants.CONFIG_BUNDLE_NAME).getString(Constants.WSS4J_POC_SERVER_URL_KEY);

	private static final int PRODUCTS_INITIAL_SIZE = 5;

	private ProductService securedService = null;

	private static final String ADMIN_LOGIN = "admin";

	private static final String ADMIN_PASSWORD = "*password@0";

	private static final String ANONYMOUS_LOGIN = "anonymous";

	private static final String ANONYMOUS_PASSWORD = "anonymous";

	/**
	 * 
	 */
	@Before
	public void before() {
		setUpNewProductRepository();
	}

	@Test
	public final void anonymousShouldList() {

		securedService = getSecuredProxy(ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);

		List<Product> products = securedService.list();

		assertNotNull(products);

		assertEquals(PRODUCTS_INITIAL_SIZE, products.size());

	}

	/**
	 * 
	 */
	@Test
	public void anonymousShouldFindByDescription() {

		String name = "doliprane";

		String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
			+ "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

		addProduct(name, description, ADMIN_LOGIN, ADMIN_PASSWORD);

		assertEquals(PRODUCTS_INITIAL_SIZE + 1, securedService.list().size());

		String term = "description";

		securedService = getSecuredProxy(ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);

		assertEquals(PRODUCTS_INITIAL_SIZE, securedService.findByDescription(term).size());

	}

	/**
	 * 
	 */
	@Test(expected=SOAPFaultException.class)
	public void anonymousShouldNotAdd() {

		String name = "doliprane";

		String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
			+ "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

		addProduct(name, description, ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);

	}

	/**
	 * 
	 */
	@Test(expected=SOAPFaultException.class)
	public void anonymousShouldNotUpdate() {

		String name = "doliprane";

		String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
			+ "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

		Long productId = addProduct(name, description, ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);


		Product p = securedService.get(productId);

		assertNotNull(p);

		String newName = "prozac";

		p.setName(newName);

		String newDescription = "Ce médicament est un antidépresseur de la famille des inhibiteurs de la recapture de la sérotonine.";

		newDescription += "Il est utilisé chez l'adulte dans le traitement : -des états dépressifs ; \n-des troubles obsessionnels compulsifs ;";

		newDescription += "\n- de la boulimie (en complément d'une psychothérapie). ;";

		p.setDescription(newDescription);

		securedService = getSecuredProxy(ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);
		
		securedService.update(p);

	}

	/**
	 * 
	 */
	@Test
	public void anonymousShouldGet() {

		String name = "doliprane";

		String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
			+ "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

		Long productId = addProduct(name, description, ADMIN_LOGIN, ADMIN_PASSWORD);

		securedService = getSecuredProxy(ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);

		Product p = securedService.get(productId);

		assertNotNull(p);

	}

	/**
	 * 
	 */
	@Test(expected=SOAPFaultException.class)
	public void anonymousShouldNotDelete() {

		String name = "doliprane";

		String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
			+ "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

		Long productId = addProduct(name, description, ADMIN_LOGIN, ADMIN_PASSWORD);

		securedService = getSecuredProxy(ANONYMOUS_LOGIN, ANONYMOUS_PASSWORD);
		
		assertNotNull(securedService.get(productId));
		
		securedService.delete(productId);

	}

	private Long addProduct(String name, String description, String login, String password) {
		securedService = getSecuredProxy(login, password);

		Product p = new Product();

		p.setName(name);

		p.setDescription(description);

		return securedService.add(p);
		
	}
	/**
	 * 
	 */
	private void setUpNewProductRepository() {

		securedService = getSecuredProxy(ADMIN_LOGIN, ADMIN_PASSWORD);

		securedService.clear();

		for (int i = 0; i < PRODUCTS_INITIAL_SIZE; i++) {

			Product p = new Product();

			String name = "name" + i;

			String description = "description" + i;

			p.setName(name);

			p.setDescription(description);

			securedService.add(p);

		}

	}

	@SuppressWarnings("rawtypes")
	private ProductService getSecuredProxy(String login, final String password) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(ProductService.class);
		factory.setAddress(WSS4J_POC_SERVER_URL);
		List<Interceptor> inInterceptors = new ArrayList<Interceptor>();
		inInterceptors.add(new LoggingInInterceptor());
		List<Interceptor> outInterceptors = new ArrayList<Interceptor>();
		outInterceptors.add(new LoggingOutInterceptor());
		outInterceptors.add(new SAAJOutInterceptor());
		final Map<String, Object> authConfig = new HashMap<String, Object>();

		authConfig.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		authConfig.put(WSHandlerConstants.USER, login);
		authConfig.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");

		authConfig.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler()
		{
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
			{
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
				pc.setIdentifier((String)authConfig.get(WSHandlerConstants.USER));
				pc.setPassword(password);
			}
		});

		outInterceptors.add(new WSS4JOutInterceptor(authConfig));
		factory.setInInterceptors(inInterceptors);
		factory.setOutInterceptors(outInterceptors);

		return (ProductService) factory.create();

	}

}
