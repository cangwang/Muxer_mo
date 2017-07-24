package cangwang.com.base.modulebus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zjl on 16/10/19.
 */

public class ELPublicHelper {
//    private List<ELModuleApi> apis = new ArrayList<>();

    private static ELPublicHelper instance;

    public static ELPublicHelper getInstance(){
        if(instance == null){
            synchronized (ELPublicHelper.class){
                if (instance == null){
                    instance = new ELPublicHelper();
                }
            }
        }
        return instance;
    }

    private static Map<Class<? extends ELModuleApi>,ELModuleApi> moduleApi = new HashMap<>();

    public <T extends ELModuleApi> T getModuleApi(Class<T> clazz){
        if(clazz == null){
            return null;
        }
        ELModuleApi api = null;
        if(moduleApi.containsKey(clazz)){
            api = moduleApi.get(clazz);
        }

        return (T)api;
    }

    public void register(Class<? extends ELModuleApi> clazz, ELModuleApi api){
        if(moduleApi.containsKey(clazz)){
            return;
        }
        moduleApi.put(clazz,api);
    }

    public void registerAll(Map<Class<? extends ELModuleApi>,ELModuleApi> allapi){
        if(allapi !=null){
            moduleApi.putAll(allapi);
        }
    }

    public void unregister(Class<? extends ELModuleApi> clazz){
        if(moduleApi.containsKey(clazz)){
            moduleApi.remove(clazz);
        }
    }

}
