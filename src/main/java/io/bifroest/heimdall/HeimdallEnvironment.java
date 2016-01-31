package io.bifroest.heimdall;

import java.nio.file.Path;

import io.bifroest.bifroest_client.BifroestClient;
import io.bifroest.bifroest_client.EnvironmentWithMutableBifroestClient;
import io.bifroest.commons.boot.InitD;
import io.bifroest.commons.environment.AbstractCommonEnvironment;

public class HeimdallEnvironment extends AbstractCommonEnvironment
        implements EnvironmentWithMutableBifroestClient {

    private BifroestClient bifroestClient;

    public HeimdallEnvironment( Path configPath, InitD init ) {
        super( configPath, init );
    }

    @Override
    public void setBifroestClient( BifroestClient client ) {
        this.bifroestClient = client;
    }

    @Override
    public BifroestClient bifroestClient() {
        return this.bifroestClient;
    }
}
