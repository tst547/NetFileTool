package cn.hy.netfiletool.net;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by temp547 on 2017/11/17 0017.
 */
public class OKHttpCallback implements Callback {

    private Netable netable;

    public OKHttpCallback(Netable netable){
        this.netable = netable;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        netable.run(call,response);
    }

    public interface Netable {
        void run(Call call, Response response);
    }
}