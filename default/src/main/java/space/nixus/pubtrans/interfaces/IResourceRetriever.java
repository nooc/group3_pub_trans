package space.nixus.pubtrans.interfaces;

import java.io.IOException;

public interface IResourceRetriever {
    
    byte[] getResourceAsBytes(String resourceName) throws IOException;
}
