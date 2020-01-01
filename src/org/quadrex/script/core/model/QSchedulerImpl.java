package org.quadrex.script.core.model;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.scheduler.QScheduler;
import org.quadrex.script.QScript;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QSchedulerImpl implements QScheduler {

    private ScriptPlugin plugin;
    private Map<Integer, QScript> tasks;
    private BukkitScheduler scheduler;

    public QSchedulerImpl(ScriptPlugin plugin) {
        this.plugin = plugin;
        tasks = new ConcurrentHashMap<>();
        scheduler = Bukkit.getScheduler();
    }

    private void addTask(QScript script, BukkitTask task) {
        tasks.put(task.getTaskId(), script);
    }

    private void removeTask(BukkitTask task) {
        tasks.remove(task.getTaskId());
    }

    @Override
    public BukkitTask runTask(QScript script, Runnable task) {
        BukkitTask bukkitTask = scheduler.runTask(plugin, task);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override @SuppressWarnings("deprecation")
    public BukkitTask runTask(QScript script, BukkitRunnable task) {
        BukkitTask bukkitTask = scheduler.runTask(plugin, task);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskAsynchronously(QScript script, Runnable task) {
        BukkitTask bukkitTask = scheduler.runTaskAsynchronously(plugin, task);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override @SuppressWarnings("deprecation")
    public BukkitTask runTaskAsynchronously(QScript script, BukkitRunnable task) {
        BukkitTask bukkitTask = scheduler.runTaskAsynchronously(plugin, task);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLater(QScript script, Runnable task, long delay) {
        BukkitTask bukkitTask = scheduler.runTaskLater(plugin, task, delay);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override @SuppressWarnings("deprecation")
    public BukkitTask runTaskLater(QScript script, BukkitRunnable task, long delay) {
        BukkitTask bukkitTask = scheduler.runTaskLater(plugin, task, delay);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(QScript script, Runnable task, long delay) {
        BukkitTask bukkitTask = scheduler.runTaskLaterAsynchronously(plugin, task, delay);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(QScript script, BukkitRunnable task, long delay) {
        BukkitTask bukkitTask = scheduler.runTaskLaterAsynchronously(plugin, task, delay);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimer(QScript script, Runnable task, long delay, long period) {
        BukkitTask bukkitTask = scheduler.runTaskTimer(plugin, task, delay, period);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimer(QScript script, BukkitRunnable task, long delay, long period) {
        BukkitTask bukkitTask = scheduler.runTaskTimer(plugin, task, delay, period);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(QScript script, Runnable task, long delay, long period) {
        BukkitTask bukkitTask = scheduler.runTaskTimerAsynchronously(plugin, task, delay, period);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(QScript script, BukkitRunnable task, long delay, long period) {
        BukkitTask bukkitTask = scheduler.runTaskTimerAsynchronously(plugin, task, delay, period);
        addTask(script, bukkitTask);
        return bukkitTask;
    }

    @Override
    public void unregisterAll(QScript script) {
        tasks.forEach((task, sc1) -> {
            if (sc1.equals(script)) {
                if (scheduler.isCurrentlyRunning(task) || scheduler.isQueued(task)) {
                    scheduler.cancelTask(task);
                }
                tasks.remove(task);
            }
        });
    }

    @Override
    public void purge() {
        tasks.forEach((task, script) -> {
            if (!scheduler.isCurrentlyRunning(task) && !scheduler.isQueued(task)) {
                tasks.remove(task);
            }
        });
    }
}
