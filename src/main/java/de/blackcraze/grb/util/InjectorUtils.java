package de.blackcraze.grb.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.blackcraze.grb.dao.IMateDao;
import de.blackcraze.grb.dao.IStockDao;
import de.blackcraze.grb.dao.IStockTypeDao;
import de.blackcraze.grb.model.entity.Mate;
import net.dv8tion.jda.core.entities.User;

import java.util.Optional;

public class InjectorUtils {

    private InjectorUtils() {
    }


    public static final Injector INJECTOR = Guice.createInjector(new DbUtil());

    public static IStockTypeDao getStockTypeDao() {
        return INJECTOR.getInstance(IStockTypeDao.class);
    }

    public static IMateDao getMateDao() {
        return INJECTOR.getInstance(IMateDao.class);
    }

    public static IStockDao getStockDao() {
        return INJECTOR.getInstance(IStockDao.class);
    }

}
