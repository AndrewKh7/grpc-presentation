package client;

import io.grpc.*;

public class ClientInterceptor implements io.grpc.ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
                System.out.println("Called Client Request Interceptor");
                super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                                responseListener) {

                            @Override
                            public void onMessage(RespT message) {
                                System.out.println("Called Client Response interceptor");
                                super.onMessage(message);
                            }
                        },headers);
            }
        };
    }
}
