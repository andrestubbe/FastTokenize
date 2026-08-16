package fasttokenize.bridge;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Native JNI bridge for AVX2 hardware-accelerated code tokenization.
 * Falls back safely to pure Java if native binary is unavailable (e.g. on macOS or Linux).
 */
public final class NativeTokenizeBridge {

    private static boolean isNativeLoaded = false;

    static {
        try {
            // Attempt loading via FastCore unified JNI loader
            fastcore.LibraryLoader.load("fasttokenize");
            isNativeLoaded = true;
        } catch (Throwable t) {
            try {
                // Direct fallback load attempt
                String libName = System.mapLibraryName("fasttokenize");
                InputStream in = NativeTokenizeBridge.class.getResourceAsStream("/win32-x86-64/" + libName);
                if (in != null) {
                    File tempFile = File.createTempFile("fasttokenize_", "_" + libName);
                    tempFile.deleteOnExit();
                    Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.load(tempFile.getAbsolutePath());
                    isNativeLoaded = true;
                } else {
                    System.loadLibrary("fasttokenize");
                    isNativeLoaded = true;
                }
            } catch (Throwable fallbackThrowable) {
                // Pure Java fallback on macOS, Linux, or systems without AVX2 DLL
                isNativeLoaded = false;
            }
        }
    }

    private NativeTokenizeBridge() {}

    public static boolean isNativeLoaded() {
        return isNativeLoaded;
    }

    public static native void scanStylesSIMD(
        byte[] inputBytes,
        int length,
        byte[] outputStyles,
        int languageId
    );
}
