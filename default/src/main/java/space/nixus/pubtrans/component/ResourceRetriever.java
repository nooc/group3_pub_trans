package space.nixus.pubtrans.component;

import java.io.IOException;

import org.springframework.stereotype.Component;

import space.nixus.pubtrans.App;
import space.nixus.pubtrans.interfaces.IResourceRetriever;

/**
 * Get embedded resources using class loader.
 */
@Component()
public final class ResourceRetriever implements IResourceRetriever {

    @Override
    public byte[] getResourceAsBytes(String resourceName) throws IOException {
        try (var strm = App.class.getClassLoader().getResourceAsStream(resourceName)) {
            return strm.readAllBytes();
        }
    }
}
