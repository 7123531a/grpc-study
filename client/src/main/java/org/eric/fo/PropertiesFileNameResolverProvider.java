package org.eric.fo;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import java.net.URI;

public class PropertiesFileNameResolverProvider extends NameResolverProvider {
    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 0;
    }

    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        return new PropertiesFileNameResolver(targetUri, args);
    }

    @Override
    public String getDefaultScheme() {
        return "fo";
    }
}
