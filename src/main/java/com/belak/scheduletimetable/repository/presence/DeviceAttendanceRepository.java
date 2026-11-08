package com.belak.scheduletimetable.repository.presence;


import com.belak.scheduletimetable.model.DeviceAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceAttendanceRepository
        extends JpaRepository<DeviceAttendance, Long> {

    boolean existsByDeviceUuidAndSeanceId(
            String deviceUuid,
            Long seanceId
    );
}
