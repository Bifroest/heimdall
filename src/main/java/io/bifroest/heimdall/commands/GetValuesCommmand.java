package io.bifroest.heimdall.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.bifroest_client.BifroestClient;
import io.bifroest.bifroest_client.BifroestClientSystem;
import io.bifroest.bifroest_client.EnvironmentWithBifroestClient;
import io.bifroest.commons.model.Interval;
import io.bifroest.commons.net.jsonserver.Command;
import io.bifroest.retentions.MetricSet;

@MetaInfServices
public class GetValuesCommmand<E extends EnvironmentWithBifroestClient> implements Command<E> {
    public static final String COMMAND = "get_values";

    private static final Logger log = LogManager.getLogger();

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
        Pair<String, Boolean> paramName = new ImmutablePair<>( "name", true );
        Pair<String, Boolean> paramStart = new ImmutablePair<>( "startTimestamp", true );
        Pair<String, Boolean> paramEnd = new ImmutablePair<>( "endTimestamp", true );
        return Arrays.asList( paramName, paramStart, paramEnd );
    }

    @Override
    public JSONObject execute( JSONObject input, E env ) {
        BifroestClient client = env.bifroestClient();
        String metricName = input.getString( "name" );
        int start = input.getInt( "startTimestamp" );
        int end = input.getInt( "endTimestamp" );
        
        MetricSet metrics = client.getMetrics( metricName, new Interval( start, end ) );
        JSONObject result = new JSONObject();
        result.put( "time_def", makeTimespec( metrics.interval(), metrics.step()) );
        result.put( "values", makeValues( metrics.values() ) );
        return result;
    }
    private static JSONObject makeTimespec( Interval interval, long frequency ) {
        JSONObject timespec = new JSONObject();
        timespec.put( "start", interval.start() );
        timespec.put( "end", interval.end() );
        timespec.put( "step", frequency );
        return timespec;
    }

    private static JSONArray makeValues( double[] metricValues ) {
        JSONArray values = new JSONArray();
        for ( int i = 0; i < metricValues.length; i++ ) {
            if ( Double.isNaN( metricValues[i] ) ) {
                values.put( i, JSONObject.NULL );
            } else {
                values.put( i, metricValues[i] );
            }
        }

        if ( log.isTraceEnabled() ) {
            int valuesFoundTrue = 0;
            for ( int ii = 0; ii < metricValues.length; ii++ ) {
                if ( Double.isNaN( metricValues[ii] ) ) {
                    valuesFoundTrue++;
                }
            }
            log.trace( "Found " + valuesFoundTrue + " values" );
        }
        return values;
    }
}
