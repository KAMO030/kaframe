package kamo.cloud;


import kamo.cloud.protocol.bio.BioSocketProtocol;


public class ProtocolFactory {
    public static Protocol getProtocol(String name) {

        switch (name) {
            case "BioSocket":
                return  new BioSocketProtocol();
            case "dubbo":
//                return new DubboProtocol();
            default:
                break;
        }

        return  new BioSocketProtocol();
    }
}
