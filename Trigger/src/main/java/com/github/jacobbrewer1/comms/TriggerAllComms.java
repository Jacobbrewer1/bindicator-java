package com.github.jacobbrewer1.comms;

import com.github.jacobbrewer1.protos.BindicatorProto;
import com.github.jacobbrewer1.protos.BindicatorServiceGrpc;
import com.github.jacobbrewer1.protos.Empty;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class TriggerAllComms {

    private final ManagedChannelBuilder channelBuilder;
    private final Channel channel;

    public TriggerAllComms(String host, String port) {
        channelBuilder = ManagedChannelBuilder.forAddress(host, Integer.parseInt(port)).usePlaintext();
        channel = channelBuilder.build();
    }

    public void sendMessage() {
        Empty empty = Empty.newBuilder().build();

        BindicatorServiceGrpc.BindicatorServiceBlockingStub blockingStub = BindicatorServiceGrpc.newBlockingStub(channel);

        Empty response = blockingStub.triggerAll(empty);
    }
}
