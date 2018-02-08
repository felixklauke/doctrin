package de.felix_klauke.doctrin.server.channel;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerChannelFactory implements ChannelFactory<ServerChannel> {

    @Override
    public ServerChannel newChannel() {
        return Epoll.isAvailable() ? new EpollServerSocketChannel() : new NioServerSocketChannel();
    }
}
