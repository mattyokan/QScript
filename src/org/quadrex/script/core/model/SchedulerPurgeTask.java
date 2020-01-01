package org.quadrex.script.core.model;

import org.bukkit.scheduler.BukkitRunnable;
import org.quadrex.script.api.scheduler.QScheduler;

public class SchedulerPurgeTask extends BukkitRunnable {

    @Override
    public void run() {
        QScheduler.get().purge();
    }
}
