/*
 * Copyright (C) Ubiwhere, LDA 2019
 *
 * The reproduction, transmission or use of this document or its contents is not permitted without express written
 * authorization.  All rights, including rights created by patent grant or registration of a utility model or design,
 * are reserved. Modifications made to this document are restricted to authorized personnel only. Technical
 * specifications and features are binding only when specifically and expressly agreed upon in a written contract.
 */

package com.ubiwhere.Ignite.config;

import com.ubiwhere.Ignite.Application;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteEvents;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.IgniteScheduler;
import org.apache.ignite.IgniteSpring;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.java.JavaLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Apache Ignite Config Settings.
 *
 * @author <a href="mailto:pmrocha@ubiwhere.com">Pedro Rocha</a>
 * @version 0.0.1
 */
@Getter
@Slf4j
@Configuration
@EnableCaching
@SuppressWarnings("all")
public class IgniteConfig {

    public static final String DEVICE_CONNECTION = "DeviceConnectionCache";
    public static final String DEVICE_COMMANDS = "DeviceCommandsCache";
    public static final String DEVICE_STATUS = "DeviceStatusCache";
    /**
     * Turns on/off ignite filepresistence.
     */
    @Value("${ignite.filePersistence.enabled}")
    private boolean filePersistence;

    /**
     * Set's the base path where ignite files should be stored.
     */
    @Value("${ignite.filePersistence.filePath}")
    private String filePersistenceFilePath;

    /**
     * Set's the path where ignite store files should be stored.
     */
    @Value("${ignite.filePersistence.storePath}")
    private String filePersistenceStorePath;

    /**
     * Set's the path where ignite walArchive files should be stored.
     */
    @Value("${ignite.filePersistence.walArchivePath}")
    private String filePersistenceWalArchivePath;

    /**
     * Set's the path where ignite walStorePath files should be stored.
     */
    @Value("${ignite.filePersistence.walStorePath}")
    private String filePersistenceWalStorePath;

    /**
     * Set's the default port for connecting into a Ignite Cluster.
     */
    @Value("${ignite.connectorPort}")
    private int connectorPort;

    @Value("${ignite.discoveryPort}")
    private int discoveryPort;

    @Value("${ignite.discoveryPortRange}")
    private int discoveryPortRange;

    /**
     * Set's the default server port range.
     */
    @Value("${ignite.serverPortRange}")
    private String serverPortRange;

    /**
     * Set's the working mode of this Ignite instance.
     */
    @Value("${ignite.kubernetes}")
    private boolean kubernetes;


    @Value("${ignite.kubernetes.namespace}")
    private String kubernetesNamespace;

    @Value("${ignite.kubernetes.service}")
    private String kubernetesService;

    /**
     * Set's the working mode of this Ignite instance.
     */
    @Value("${ignite.clientMode}")
    private boolean clientMode;

    /**
     * Set's the log frequency for all ignite metrics.
     */
    @Value("${ignite.metricsLogFrequency}")
    private int metricsLogFrequency;

    /**
     * Set's the size of the thread pool that manages queries.
     */
    @Value("${ignite.queryThreadPoolSize}")
    private int queryThreadPoolSize;

    /**
     * Set's the size of the thread pool that manages devices Stream.
     */
    @Value("${ignite.dataStreamerThreadPoolSize}")
    private int dataStreamerThreadPoolSize;

    /**
     * Set's the size of the thread pool that controlls management workers.
     */
    @Value("${ignite.managementThreadPoolSize}")
    private int managementThreadPoolSize;

    /**
     * Set's the size of the public thread pool.
     */
    @Value("${ignite.publicThreadPoolSize}")
    private int publicThreadPoolSize;

    /**
     * Set's the size of the system thread pool.
     */
    @Value("${ignite.systemThreadPoolSize}")
    private int systemThreadPoolSize;

    /**
     * Set's the size of the rebalance thread pool.
     */
    @Value("${ignite.rebalanceThreadPoolSize}")
    private int rebalanceThreadPoolSize;

    /**
     * Set's the size of the async callback thread pool.
     */
    @Value("${ignite.asyncCallbackPoolSize}")
    private int asyncCallbackPoolSize;

    /**
     * Set's on/off peer class loading.
     */
    @Value("${ignite.peerClassLoadingEnabled}")
    private boolean peerClassLoadingEnabled;

    /**
     * Set's the instance name.
     */
    @Value("${ignite.instanceName:#{null}}")
    private String instanceName;

    /**
     * Set's on/off compact footer.
     */
    @Value("${ignite.compactFooter}")
    private boolean compactFooter;

    /**
     * Set's the address pattern for discovering new ignite instances.
     */
    @Value("${ignite.autoDiscoverAddresses}")
    private String autoDiscoverAddresses;

    /**
     * Set's the size of the async callback thread pool.
     */
    @Value("${ignite.slowClientQueueLimit}")
    private int slowClientQueueLimit;

    @Value("${ignite.defaultPort}")
    private int defaultPort;

    @Value("${ignite.defaultPortRange}")
    private int defaultPortRange;

    @Value("${ignite.minimumClusterSize}")
    private int minimumClusterSize;

    /**
     * Specifies if ssl is enabled.
     */
    @Value("${ignite.ssl.enabled:false}")
    private Boolean sslEnabled;

    /**
     * Specifies the key store file location.
     */
    @Value("${ignite.ssl.keyStore:#{null}}")
    private String sslKeyStore;

    /**
     * Specifies the key store file password.
     */
    @Value("${ignite.ssl.keyStorePassword:#{null}}")
    private String sslKeyStorePassword;

    /**
     * Specifies the trust store file location.
     */
    @Value("${ignite.ssl.trustStore:#{null}}")
    private String sslTrustStore;

    /**
     * Specifies the trust store file password.
     */
    @Value("${ignite.ssl.trustStorePassword:#{null}}")
    private String sslTrustStorePassword;


    /**
     * Returns a new instance of IgniteLogger using Slf4 logger used by spring.
     *
     * @return IgniteLogger
     */
    @Bean
    public IgniteLogger igniteLogger() {
        return new JavaLogger();
    }

    /**
     * Returns a new sslContextFactory to enable ignite tls connections.
     *
     * @return SslContextFactory
     */
    @Bean
    @ConditionalOnProperty(prefix = "ignite", name = "ssl.enabled", havingValue = "true")
    public SslContextFactory sslContextFactory() {
        if (this.getSslEnabled()
            && this.getSslKeyStore() != null
            && this.getSslTrustStore() != null) {

            SslContextFactory factory = new SslContextFactory();

            factory.setKeyStoreFilePath(this.getSslKeyStore());
            factory.setKeyStorePassword(this.getSslKeyStorePassword().toCharArray());

            factory.setTrustStoreFilePath(this.getSslTrustStore());
            factory.setTrustStorePassword(this.getSslTrustStorePassword().toCharArray());

            return factory;
        } else {
            return new SslContextFactory();
        }

    }

    /**
     * Set's Apache Ignite startup settings with Tls Support.
     *
     * @return the Apache Ignite configuration object
     */
    @Bean
    @ConditionalOnProperty(prefix = "ignite", name = "ssl.enabled", havingValue = "true")
    @SuppressWarnings("unchecked")
    public IgniteConfiguration igniteSslConfiguration(IgniteLogger logger, SslContextFactory sslContextFactory) {
        IgniteConfiguration igniteConfiguration = getConfiguration(logger);

        log.debug("IgniteSSL: {}", getSslEnabled());

        if (getSslEnabled() && sslContextFactory != null) {
            igniteConfiguration.setSslContextFactory(sslContextFactory);
        }

        return igniteConfiguration;
    }

    /**
     * Set's Apache Ignite startup settings.
     *
     * @return the Apache Ignite configuration object
     */
    @Bean
    @ConditionalOnProperty(prefix = "ignite", name = "ssl.enabled", havingValue = "false")
    @SuppressWarnings("unchecked")
    public IgniteConfiguration igniteConfiguration(IgniteLogger logger) {
        return getConfiguration(logger);
    }

    /**
     * Returns the base ignite configuration object.
     *
     * @param logger Ignite logger
     * @return Ingite Configuration object.
     */
    private IgniteConfiguration getConfiguration(IgniteLogger logger) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(false);
        igniteConfiguration.setActiveOnStart(true);


        if (getInstanceName() != null) {
            instanceName = Application.INSTANCE_ID;
            igniteConfiguration.setConsistentId(Application.INSTANCE_ID);
            igniteConfiguration.setIgniteInstanceName(Application.INSTANCE_ID);
        } else {
            instanceName = UUID.randomUUID().toString();
            igniteConfiguration.setConsistentId(instanceName);
            igniteConfiguration.setIgniteInstanceName(instanceName);
        }


        // common ignite configuration
//        igniteConfiguration.setMetricsLogFrequency(getMetricsLogFrequency());
//        igniteConfiguration.setQueryThreadPoolSize(getQueryThreadPoolSize());
//        igniteConfiguration.setDataStreamerThreadPoolSize(getDataStreamerThreadPoolSize());
//        igniteConfiguration.setManagementThreadPoolSize(getManagementThreadPoolSize());
//        igniteConfiguration.setPublicThreadPoolSize(getPublicThreadPoolSize());
//        igniteConfiguration.setSystemThreadPoolSize(getSystemThreadPoolSize());
//        igniteConfiguration.setRebalanceThreadPoolSize(getRebalanceThreadPoolSize());
//        igniteConfiguration.setAsyncCallbackPoolSize(getAsyncCallbackPoolSize());


        igniteConfiguration.setPeerClassLoadingEnabled(isPeerClassLoadingEnabled());

        BinaryConfiguration binaryConfiguration = new BinaryConfiguration();
        binaryConfiguration.setCompactFooter(isCompactFooter());
        igniteConfiguration.setBinaryConfiguration(binaryConfiguration);

        TcpCommunicationSpi tcpCommunicationSpi = new TcpCommunicationSpi();
        tcpCommunicationSpi.setLocalPort(getDefaultPort());
        tcpCommunicationSpi.setLocalPortRange(getDefaultPortRange());
        igniteConfiguration.setCommunicationSpi(tcpCommunicationSpi);

        // cluster tcp configuration
        if (isKubernetes()) {
            TcpDiscoveryKubernetesIpFinder kubernetesIpFinder = new TcpDiscoveryKubernetesIpFinder();
            kubernetesIpFinder.setNamespace(getKubernetesNamespace());
            kubernetesIpFinder.setServiceName(getKubernetesService());

            TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
            tcpDiscoverySpi.setLocalPort(getDiscoveryPort());
            tcpDiscoverySpi.setLocalPortRange(getDiscoveryPortRange());
            tcpDiscoverySpi.setIpFinder(kubernetesIpFinder);

            igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        } else {
            TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
            ipFinder.setAddresses(new ArrayList<String>() {
                {
                    add(getAutoDiscoverAddresses());
                }
            });


            TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
            tcpDiscoverySpi.setLocalPort(getDiscoveryPort());
            tcpDiscoverySpi.setLocalPortRange(getDiscoveryPortRange());
            tcpDiscoverySpi.setIpFinder(ipFinder);

            igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        }


        log.info(
            "IgniteConfigPorts: LocalPort: {} | LocalPortRange: {} | DiscoveryPort: {} | DiscoveryPortRange: {}"
                + " | AutoDiscoverAddresses: {} | InstanceName: {} ",
            getDefaultPort(),
            getDefaultPortRange(),
            getDiscoveryPort(),
            getDiscoveryPortRange(),
            getAutoDiscoverAddresses(),
            getInstanceName()
        );


        // Ignite LifeCycleBean
        //igniteConfiguration.setLifecycleBeans(new CustomLifecycleBean());


        //igniteConfiguration.setFailureDetectionTimeout(1000);
        //igniteConfiguration.setGridLogger(logger);


        // cache configuration

      /*  CacheConfiguration deviceConnectionCache = new CacheConfiguration();
        deviceConnectionCache.setCopyOnRead(false);
        // as we have one node for now
        deviceConnectionCache.setBackups(1);
        deviceConnectionCache.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        deviceConnectionCache.setName(DEVICE_CONNECTION);
        deviceConnectionCache.setIndexedTypes(String.class, Connection.class);

        CacheConfiguration deviceCommandsCache = new CacheConfiguration();
        deviceCommandsCache.setCopyOnRead(false);
        // as we have one node for now
        deviceCommandsCache.setBackups(1);
        deviceCommandsCache.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        deviceCommandsCache.setName(DEVICE_COMMANDS);
        deviceCommandsCache.setIndexedTypes(String.class, Command.class);

        igniteConfiguration.setCacheConfiguration(deviceConnectionCache, deviceCommandsCache); */

        return igniteConfiguration;
    }

    /**
     * Instantiates a new Apache Ignite instance for autowiring purposes.
     *
     * @param igniteConfiguration igniteConfiguration object
     * @return a pointer to a new ignite instance
     * @throws IgniteException something wrong happened when trying to instantiate a new client/server
     */
    @Bean(destroyMethod = "close", name = "ignite")
    @SuppressWarnings("all")
    public Ignite ignite(IgniteConfiguration igniteConfiguration, ApplicationContext context)
        throws IgniteCheckedException, RuntimeException {
        final Ignite ignite = IgniteSpring.start(igniteConfiguration, context);

        ignite.cluster().active(true);

        log.info("IgniteClusterActive: {}", ignite.cluster().active());


        return ignite;
    }


    /**
     * Starts a new instance of the IgniteEvents service.
     *
     * @param ignite ignite instance
     * @return IgniteEvents
     */
    @Bean
    @DependsOn("ignite")
    public IgniteEvents events(Ignite ignite) {
        return ignite.events();
    }


    /**
     * Starts a new instance of the IgniteMessaging service.
     *
     * @param ignite ignite instance
     * @return a new IgniteMessaging instance
     */
    @Bean
    @SuppressWarnings("all")
    public IgniteMessaging messaging(Ignite ignite) throws RuntimeException {
        if (ignite == null) {
            throw new RuntimeException("Ignite failed to start");
        }

        return ignite.message();
    }

    /**
     * Instantiates Ignite as the Spring Cache manager.
     *
     * @param context spring application context
     * @return a new instance of CacheManager
     */
    @Bean("cacheManager")
    @DependsOn("ignite")
    public SpringCacheManager cacheManager(IgniteConfiguration configuration, ApplicationContext context) {
        final SpringCacheManager cacheManager = new SpringCacheManager();

        cacheManager.setApplicationContext(context);
        cacheManager.setIgniteInstanceName(instanceName);

        return cacheManager;
    }


    /**
     * Instantiates Ignite Scheduler as a Spring Bean.
     *
     * @param ignite ignite instance bean
     * @return a new instance of IgniteScheduler
     * @throws RuntimeException if Ignite has not been initialized
     */
    @Bean
    @DependsOn("ignite")
    @SuppressWarnings("all")
    public IgniteScheduler scheduler(Ignite ignite) throws RuntimeException {
        if (ignite == null) {
            throw new RuntimeException("Ignite failed to start");
        }
        return ignite.scheduler();
    }


}
