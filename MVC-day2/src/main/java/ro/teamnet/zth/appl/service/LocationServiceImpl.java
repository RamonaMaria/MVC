package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.dao.LocationDao;
import ro.teamnet.zth.appl.domain.Location;

import java.util.List;

/**
 * Created by Ramona.Raducu on 7/21/2017.
 */
public
class LocationServiceImpl implements  LocationService {
    private final LocationDao locationDao = new LocationDao();
    @Override
    public
    List<Location> findAll() {
        return locationDao.getAllLocations();
    }

    @Override
    public
    Location findOne(Long locationId) {
        return locationDao.getLocationById(locationId);
    }
}
