package com.github.jacobbrewer1.services;

import com.github.jacobbrewer1.businesslogic.TriggerAllLogic;
import com.github.jacobbrewer1.logging.Logging;
import com.github.jacobbrewer1.protos.BindicatorServiceGrpc;
import com.github.jacobbrewer1.protos.Empty;
import com.google.gson.Gson;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

public class BindicatorService extends BindicatorServiceGrpc.BindicatorServiceImplBase {

    private final Logging logging;

    private final Gson gson;

    private final TriggerAllLogic triggerAllLogic;

    public BindicatorService(Logging logging, Gson gson, TriggerAllLogic triggerAllLogic) {
        this.logging = logging;
        this.gson = gson;
        this.triggerAllLogic = triggerAllLogic;
    }

    @Override
    public void triggerAll(Empty request, StreamObserver<Empty> responseObserver) {
        logging.logInfo("triggerAll message received");

        try {
            triggerAllLogic.execute();
        } catch (Exception exception) {
            exception.printStackTrace();

            Status status = Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage(exception.getMessage())
                    .build();

            responseObserver.onError(StatusProto.toStatusRuntimeException(status));
            return;
        }

        responseObserver.onNext(null);
        responseObserver.onCompleted();

        logging.logInfo("triggerAll completed gracefully");
    }
}
