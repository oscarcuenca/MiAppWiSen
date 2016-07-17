package com.amg_eservices.miappwisen;

public class ApiClient {
    public final static String message_error = "Ã¦ÂœÂÃ¥ÂŠÂ¡Ã¥Â™Â¨Ã¨Â¿ÂžÃ¦ÂŽÂ¥Ã¦ÂœÂ‰Ã©Â—Â®Ã©Â¢Â˜";

    public interface ClientCallback {
        abstract void onSuccess(Object data);

        abstract void onFailure(String message);

        abstract void onError(Exception e);
    }
}
