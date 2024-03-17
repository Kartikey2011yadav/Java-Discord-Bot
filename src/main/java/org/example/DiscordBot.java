package org.example;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.commands.CommandManager;
import org.example.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;


import javax.security.auth.login.LoginException;
import java.util.Objects;

public class DiscordBot extends ListenerAdapter {
    private final ShardManager shardManager;
    private final Dotenv config;

    /**
     * Loads environment variables and builds the bot shard manager.
     * @throws LoginException occurs when bot token is invalid.
     */
    public DiscordBot() throws LoginException {

        config = Dotenv.configure().load();
        String Token = config.get("TOKEN");
        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(Token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setEventPassthrough(true);
        builder.setActivity(Activity.playing("With Kids"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());



    }

    /**
     * Retrieves the bot environment variables.
     * @return the DotEnv instance for the bot.
     */
    public Dotenv getConfig() { return config; }

    /**
     * Retrieves the bot shard manager.
     * @return the ShardManager instance for the bot.
     */
    public ShardManager getShardManager() { return shardManager; }

    public static void main(String[] args){
        try{
            DiscordBot bot = new DiscordBot();
        }
        catch (LoginException e){
            System.err.println("ERROR: Invalid Token");
        }

    }

}
