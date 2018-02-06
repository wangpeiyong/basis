package org.wpy.spi;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description:
 * @Package org.wpy.spi
 * @date 2017/12/4 15:42
 * @see
 */
public class ToUpperCaseBaseService implements IBaseService {

    @Override
    public String echo(String args) {
        return args.toUpperCase();
    }
}
