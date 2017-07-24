package cangwang.com.base.basecomponent;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cangwang.com.base.modulebus.ELModuleApi;
import cangwang.com.base.modulebus.ELPublicHelper;

/**
 * Created by air on 16/10/19.
 */

public abstract class BasePresenter  {
    Map<Class<? extends ELModuleApi>,ELModuleApi> map;
    public BasePresenter(){
        if (map == null)
            map = getModuleApi();

        if(map !=null && !map.isEmpty() ) {
            ELPublicHelper.getInstance().registerAll(map);
        }
    }

    public void onDestroy(){
        if(map == null)
            map = getModuleApi();

        if(map !=null && !map.isEmpty() ) {
            Set<Class<? extends ELModuleApi>> keySet = map.keySet();
            Iterator<Class<? extends ELModuleApi>> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                ELPublicHelper.getInstance().unregister(iterator.next());
            }
        }
    }
    public abstract Map<Class<? extends ELModuleApi>,ELModuleApi> getModuleApi();
}
