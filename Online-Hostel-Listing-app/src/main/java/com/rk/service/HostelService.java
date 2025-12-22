package com.rk.service;

import java.util.List;
import java.util.Map;

import com.rk.dto.FloorDTO;
import com.rk.dto.HostelDTO;
import com.rk.dto.RoomDTO;
import com.rk.dto.RoomTypeDTO;

public interface HostelService {
	
	public List<FloorDTO> fetchAllFloors(Long hostelId) throws Exception;
	public List<RoomDTO> fetchAllRooms(Long hostelId) throws Exception;
	public List<HostelDTO> getFilteredHostels(Map<String, Object> filters) throws Exception;
	public List<RoomTypeDTO> getAllRoomTypeByHostelId(Long hostelId)throws Exception;


	
}
