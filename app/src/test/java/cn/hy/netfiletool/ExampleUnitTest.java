package cn.hy.netfiletool;

import cn.hy.netfiletool.common.WifiUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(WifiUtil.long2ip(WifiUtil.ip2long("192.168.1.101")));
    }
}