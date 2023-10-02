package com.caiotakabatake.jdaclass;

import com.caiotakabatake.jdaclass.commands.CommandManager;
import com.caiotakabatake.jdaclass.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class JdaClass {
    private final Dotenv config;

    private final ShardManager shardManager;

    public JdaClass() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        System.out.println(token);
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.watching("Caio Takabatake"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
//        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
//        builder.setChunkingFilter(ChunkingFilter.ALL);
//        builder.enableCache(CacheFlag.ONLINE_STATUS);
        this.shardManager = builder.build();

        shardManager.addEventListener(new EventListener(), new CommandManager());
    }

    public Dotenv getConfig() {
        return this.config;
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public static void main(String[] args) {
        try {
            JdaClass bot = new JdaClass();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided token is invalid!");
        }
    }
}
