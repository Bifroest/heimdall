package com.goodgame.profiling.bifroest_heimdall.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import com.goodgame.profiling.bifroest.bifroest_client.BasicClient.MetricState;
import com.goodgame.profiling.bifroest.bifroest_client.BifroestClient;
import com.goodgame.profiling.bifroest.bifroest_client.BifroestClientSystem;
import com.goodgame.profiling.bifroest.bifroest_client.EnvironmentWithBifroestClient;
import com.goodgame.profiling.commons.systems.net.jsonserver.Command;

@MetaInfServices
public class GetSubMetricsCommand<E extends EnvironmentWithBifroestClient> implements Command<E> {
    public static final String COMMAND = "get-sub-metrics";

    @Override
    public void addRequirements( Collection<String> requirements ) {
        requirements.add( BifroestClientSystem.IDENTIFIER );
    }

    @Override
    public String getJSONCommand() {
        return COMMAND;
    }

    @Override
    public List<Pair<String, Boolean>> getParameters() {
        return Collections.<Pair<String, Boolean>> singletonList( new ImmutablePair<>( "query", true ) );
    }

    @Override
    public JSONObject execute( JSONObject input, E env ) {
        BifroestClient client = env.bifroestClient();
        String query = input.getString( "query" );
        Map<String, MetricState> subMetrics = client.getSubMetrics( query );
        JSONObject retval = new JSONObject();
        JSONArray resultsArray = new JSONArray();

        for ( Map.Entry<String, MetricState> result : subMetrics.entrySet() ) {
            JSONObject oneResult = new JSONObject();
            oneResult.put( "path", result.getKey() );
            oneResult.put( "isLeaf", result.getValue() == MetricState.LEAF );
            resultsArray.put( oneResult );
        }

        retval.put( "results", resultsArray );
        return retval;
    }

}
