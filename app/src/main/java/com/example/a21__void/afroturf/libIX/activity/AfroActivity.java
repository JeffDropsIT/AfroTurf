package com.example.a21__void.afroturf.libIX.activity;


import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.fragment.ProcessErrorFragment;
import com.example.a21__void.afroturf.manager.ConnectionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created by ASANDA on 2019/07/12.
 * for Pandaphic
 */
public abstract class AfroActivity extends AppCompatActivity implements AfroFragment.InteractionListener, ProcessErrorFragment.InteractionListener {
    private final Map<AfroFragment, List<Integer>> queue = new HashMap<>();
    private int activeBackgroundWorkCount = 0;

    protected abstract void onWorkModeChange(int mode);

    protected void requestOnlineWorkingMode(){
        AfroFragment[] fragments = queue.keySet().toArray(new AfroFragment[queue.keySet().size()]);

        for(int pos = 0; pos < fragments.length; pos++){
            AfroFragment fragment = fragments[pos];
            List<Integer> _pids = queue.remove(fragment);
            Integer[] pids = _pids.toArray(new Integer[_pids.size()]);

            for(int i = 0; i < pids.length; i++){
                fragment.retryProcess(pids[pos]);
            }
        }

        ConnectionManager connectionManager = ConnectionManager.getInstance(this.getApplicationContext());
        if(queue.size() > 0 && connectionManager.getWorkingMode() == ConnectionManager.WORKING_MODE_ONLINE){
            connectionManager.setWorkingMode(ConnectionManager.WORKING_MODE_OFFLINE);
            this.onWorkModeChange(ConnectionManager.WORKING_MODE_OFFLINE);
        }else{
            connectionManager.setWorkingMode(ConnectionManager.WORKING_MODE_ONLINE);
            this.onWorkModeChange(ConnectionManager.WORKING_MODE_ONLINE);
        }
    }

    public int getActiveBackgroundWorkCount() {
        return activeBackgroundWorkCount;
    }

    @Override
    public void onDoBackgroundWork() {
        this.activeBackgroundWorkCount++;
    }

    @Override
    public void onFinishBackgroundWork() {
        this.activeBackgroundWorkCount--;
    }

    @Override
    public void addToOnConnectionQueue(AfroFragment fragment, int pid) {
        if(!queue.containsKey(fragment)){
            queue.put(fragment, Collections.singletonList(pid));
        }else{
            if(!queue.get(fragment).contains(pid))
                queue.get(fragment).add(pid);
        }

        Log.i("TGIS", "addToOnConnectionQueue: " + queue.get(fragment).size());
        ConnectionManager connectionManager = ConnectionManager.getInstance(this.getApplicationContext());

        connectionManager.setWorkingMode(ConnectionManager.WORKING_MODE_OFFLINE);
        this.onWorkModeChange(ConnectionManager.WORKING_MODE_OFFLINE);

    }

    @Override
    public void removeAtOnConnectionQueue(AfroFragment fragment) {
        if(this.queue.containsKey(fragment))
            this.queue.remove(fragment);
    }
}
