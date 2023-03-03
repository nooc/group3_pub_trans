package space.nixus.pubtrans.interfaces;

import java.io.IOException;

/**
 * Get resources.
 */
public interface IResourceRetriever {
    
    byte[] getResourceAsBytes(String resourceName) throws IOException;
}
