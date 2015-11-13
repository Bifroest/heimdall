package com.goodgame.profiling.bifroest_heimdall;

import java.nio.file.Path;

import com.goodgame.profiling.bifroest.bifroest_client.BifroestClient;
import com.goodgame.profiling.bifroest.bifroest_client.EnvironmentWithMutableBifroestClient;
import com.goodgame.profiling.commons.boot.InitD;
import com.goodgame.profiling.commons.systems.common.AbstractCommonEnvironment;

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
