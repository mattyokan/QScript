# [ABANDONED] QScript - A groovy script system for Spigot/Bukkit

**This has not been in development since April of 2019. It is completely unsupported and is just here for anyone who is thinking of attempting something similar.**

I wrote this over the course of a day as I was interested in seeing what advantages/disadvantages there would be to using a combination of groovy/scripts on a live server.

It has several advantages and disadvantages, but the jist of it is that it is difficult to have fully modular scripting (due to how classloaders and GCing works) so it is very difficult (at least with how I implemented it) to have fully hotswappable scripts.

This current version allows for (somewhat) working scripts that can have menus, commands, and listeners and be loaded/unloaded in runtime.
