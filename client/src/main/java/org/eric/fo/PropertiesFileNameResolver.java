package org.eric.fo;

import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertiesFileNameResolver extends NameResolver {

    private Listener2 listener;

    private String authority;

    private Executor executor;

    private boolean resolved;

    public PropertiesFileNameResolver(URI name,
                                      Args args) {
        this.authority = name.getAuthority();
        this.executor = args.getOffloadExecutor();
    }

    @Override
    public String getServiceAuthority() {
        return authority;
    }

    @Override
    public void shutdown() {

    }


    @Override
    public void start(Listener2 listener) {
        this.listener = listener;
        resolve();
    }

    @Override
    public void refresh() {
        resolve();
    }

    private void resolve() {
        if(resolved){
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    System.out.println("resolving");

                    InetAddress address = InetAddress.getLocalHost();

                    // Each address forms an EAG
                    List<EquivalentAddressGroup> servers = new ArrayList<>(2);

                    servers.add(new EquivalentAddressGroup(new InetSocketAddress(address, 9000)));
                    servers.add(new EquivalentAddressGroup(new InetSocketAddress(address, 9001)));

                    ResolutionResult.Builder resolutionResultBuilder = ResolutionResult.newBuilder();
                    resolutionResultBuilder.setAddresses(servers);

                    listener.onResult(resolutionResultBuilder.build());
                } catch (UnknownHostException e) {
                    listener.onError(Status.UNAVAILABLE.withDescription("Unable to resolve host ").withCause(e));
                } finally {
                    resolved = true;
                }
            }
        });
    }
}
