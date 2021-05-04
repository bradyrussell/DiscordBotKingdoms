package com.bradyrussell;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.game.commands.CommandCreateKingdom;
import com.bradyrussell.game.commands.CommandSurrenderKingdom;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.OkHttpClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, LoginException {
        BasicConfigurator.configure();

        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(8, r -> new Thread(r, "KingdomThreadpoolThead"));

        EventWaiter eventWaiter = new EventWaiter();

        CommandClientBuilder commandBuilder = new CommandClientBuilder();

        final Activity activity = null;

        CommandClient commandClient = commandBuilder.setPrefix(".").setAlternativePrefix("kingdom ").addCommands(
                new CommandCreateKingdom(eventWaiter),
                new CommandSurrenderKingdom(eventWaiter)
        ).setOwnerId("374003555478142997").setActivity(activity).build();

        JDA jda = JDABuilder.createDefault(args[0]).setActivity(activity).setLargeThreshold(20).addEventListeners(commandClient, eventWaiter).build();
        jda.awaitReady();
        System.out.println("Logged in!");

        threadPool.scheduleWithFixedDelay(() -> {
            System.out.println("Scheduled event!");
        }, 0, 2, TimeUnit.HOURS);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();
            String[] commands = command.split(" ");

            if(commands.length <= 0) continue;

            if(commands[0].equalsIgnoreCase("quit")) {
                System.out.println("Shutting down...");
                OkHttpClient client = jda.getHttpClient();
                client.connectionPool().evictAll();
                client.dispatcher().executorService().shutdown();
                jda.shutdown();
                System.out.println("Shut down!");
                break;
            } else if(commands[0].equalsIgnoreCase("watch")) {
                jda.getPresence().setActivity(Activity.watching(command.replace("watch","")));
                break;
            }
        }
    }
}
