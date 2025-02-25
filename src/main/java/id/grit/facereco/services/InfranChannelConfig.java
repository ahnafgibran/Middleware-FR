package id.grit.facereco.services;

import java.net.InetSocketAddress;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
// import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

@Configuration
public class InfranChannelConfig {
        @Bean(name = "facedexChannel")
        ManagedChannel getFaceDexChannel() {
                NameResolverProvider nameResolverFactory = new MultiAddressNameResolverFactory(
                                // new InetSocketAddress("194.233.90.148", 50001),
                                // new InetSocketAddress("194.233.90.148", 50002),
                                //new InetSocketAddress("62.72.44.5", 8868));
                                new InetSocketAddress("103.249.19.25", 8868));

                // NameResolverRegistry.getDefaultRegistry().register(nameResolverFactory);

                ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost")
                                .nameResolverFactory(nameResolverFactory)
                                .defaultLoadBalancingPolicy("round_robin")
                                .usePlaintext()
                                .build();

                return channel;
        }

        @Bean(name = "facerecChannel")
        ManagedChannel getFaceRecChannel() {
                NameResolverProvider nameResolverFactory = new MultiAddressNameResolverFactory(
                                // new InetSocketAddress("194.233.90.148", 50001),
                                // new InetSocketAddress("194.233.90.148", 50002),
                                // new InetSocketAddress("109.123.239.243", 50051),
                                //new InetSocketAddress("62.72.44.5", 8868));
                                new InetSocketAddress("103.249.19.25", 8868));

                // NameResolverRegistry.getDefaultRegistry().register(nameResolverFactory);

                ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost")
                                .nameResolverFactory(nameResolverFactory)
                                .defaultLoadBalancingPolicy("round_robin")
                                .usePlaintext()
                                .build();

                return channel;
        }
}

