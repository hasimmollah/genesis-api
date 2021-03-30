package com.hasim.genesis.api.config;

import static com.hasim.genesis.api.constant.CommonConstant.REQUEST_TASK_EXECUTOR;
import static com.hasim.genesis.api.constant.CommonConstant.REST_TEMPLATE;
import static com.hasim.genesis.api.constant.CommonConstant.SCHEME_HTTP;
import static com.hasim.genesis.api.constant.CommonConstant.SCHEME_HTTPS;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * This class acts as factory of RestTemplate beans and decouples the
 * RestTemplate bean creation from the application proxy services. It overrides
 * the underlying HTTP connection properties such as HTTP connection pooling,
 * HTTP read timeout and socket connection timeout and creates a RestTemplate
 * bean which is specific to endpoint of downstream API.
 *
 * It also manages the request threadpool
 *
 */
@Configuration
public class ApplicationResourcePoolConfiguration {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Value("${request.executor.threadpool.coreSize:20}")
	int requestTaskExecutorThreadPoolCoreSize;
	@Value("${request.executor.threadpool.maxSize:50}")
	int requestTaskExecutorThreadPoolMaxSize;
	@Value("${request.executor.threadpool.queueSize:20}")
	int requestTaskExecutorThreadQueueCapacity;
	@Value("${retrieve.data.httpConnectionMaxPerRoute:50}")
	int retrieveDataHttpConnectionMaxPerRoute;
	@Value("${retrieve.data.httpConnectionMaxSize:50}")
	int retrieveDataHttpConnectionMaxSize;
	@Value("${retrieve.data.httpConnectionTimeout:500}")
	int retrieveDataHttpConnectionTimeout;
	@Value("${retrieve.data.httpReadTimeout:2000}")
	int retrieveDataHttpReadTimeout;
	@Value("${retrieve.data.httpRequestCloseIdleConnectionsInSecond:5}")
	int httpRequestCloseIdleConnectionsInSecond;

	/**
	 * Returns thread pool executor which is referred by CompletableFuture to
	 * execute requests.
	 * 
	 * @return ConcurrentTaskExecutor
	 */
	@Bean(name = REQUEST_TASK_EXECUTOR)
	public ConcurrentTaskExecutor getConcurrentRequestTaskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(requestTaskExecutorThreadPoolCoreSize);
		executor.setMaxPoolSize(requestTaskExecutorThreadPoolMaxSize);
		executor.setThreadNamePrefix(REQUEST_TASK_EXECUTOR);
		executor.setQueueCapacity(requestTaskExecutorThreadQueueCapacity);
		executor.initialize();
		return new ConcurrentTaskExecutor(executor);
	}

	/**
	 * Creates RestTemplate bean to execute HTTP requests to retrieve contact info
	 * from TSYS. It overrides HTTP connection timeout and HTTP connection socket
	 * timeout
	 * 
	 * @return RestTemplate
	 */
	@Bean(name = REST_TEMPLATE)
	public RestTemplate retrieveDataRestTemplate() {
		final PoolingHttpClientConnectionManager clientConnectionManager = getPoolingHttpClientConnectionManager(
				retrieveDataHttpConnectionMaxPerRoute, retrieveDataHttpConnectionMaxSize);
		final CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(clientConnectionManager)
				.build();

		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = getHttpRequestFactory(
				retrieveDataHttpConnectionTimeout, retrieveDataHttpReadTimeout);
		clientHttpRequestFactory.setHttpClient(httpClient);

		final RestTemplate restTemplate = restTemplateBuilder.build();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		return restTemplate;
	}

	/**
	 * Creates http client request factory with specified HTTP connection timeout
	 * and HTTP read timeout
	 * 
	 * @param httpConnectTimeout HTTP Connection Timeout
	 * @param httpReadTimeout    HTTP Read Timeout
	 * @return httpComponentsClientHttpRequestFactory
	 */
	private HttpComponentsClientHttpRequestFactory getHttpRequestFactory(final Integer httpConnectTimeout,
			final Integer httpReadTimeout) {
		final HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setConnectTimeout(httpConnectTimeout);
		httpComponentsClientHttpRequestFactory.setReadTimeout(httpReadTimeout);
		return httpComponentsClientHttpRequestFactory;
	}

	/**
	 * Creates client connection manager with specified max pool size and per route
	 * 
	 * @param httpConnectionPerRouteMaxSize HTTP CONNECTION per route max size
	 * @param httpConnectionMaxPoolSize     HTTP connection max size
	 * @return poolingHttpClientConnectionManager
	 */
	private PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(
			final Integer httpConnectionPerRouteMaxSize, final Integer httpConnectionMaxPoolSize) {
		final Registry registry = RegistryBuilder.create()
				.register(SCHEME_HTTP, PlainConnectionSocketFactory.getSocketFactory())
				.register(SCHEME_HTTPS, SSLConnectionSocketFactory.getSystemSocketFactory()).build();

		final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(
				registry);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpConnectionPerRouteMaxSize);
		poolingHttpClientConnectionManager.setMaxTotal(httpConnectionMaxPoolSize);
		poolingHttpClientConnectionManager.closeExpiredConnections();
		poolingHttpClientConnectionManager.closeIdleConnections(httpRequestCloseIdleConnectionsInSecond,
				TimeUnit.SECONDS);
		return poolingHttpClientConnectionManager;
	}

}
