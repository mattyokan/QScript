package org.quadrex.script.api.scheduler;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.QScript;

public interface QScheduler {

    static QScheduler get() {
        return ScriptPlugin.getInstance().getQScheduler();
    }

    BukkitTask runTask(QScript script, Runnable task);

    BukkitTask runTask(QScript script, BukkitRunnable task);

    BukkitTask runTaskAsynchronously(QScript script, Runnable task);

    BukkitTask runTaskAsynchronously(QScript script, BukkitRunnable task);
    
    BukkitTask runTaskLater(QScript script, Runnable task, long delay);

    BukkitTask runTaskLater(QScript script, BukkitRunnable task, long delay);

    BukkitTask runTaskLaterAsynchronously(QScript script, Runnable task, long delay);

    BukkitTask runTaskLaterAsynchronously(QScript script, BukkitRunnable task, long delay);

    BukkitTask runTaskTimer(QScript script, Runnable task, long delay, long period);

    BukkitTask runTaskTimer(QScript script, BukkitRunnable task, long delay, long period);

    BukkitTask runTaskTimerAsynchronously(QScript script, Runnable task, long delay, long period);

    BukkitTask runTaskTimerAsynchronously(QScript script, BukkitRunnable task, long delay, long period);

    void unregisterAll(QScript script);

    void purge();


}
