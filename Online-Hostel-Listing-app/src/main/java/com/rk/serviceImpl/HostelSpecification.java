package com.rk.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.rk.entity.Gender;
import com.rk.entity.Hostel;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class HostelSpecification {

	public static Specification<Hostel> filter(Map<String, Object> filters) {
	    return (root, query, cb) -> {

	        List<Predicate> predicates = new ArrayList<>();

	        // ------- CITY FILTER -------
	        if (filters.get("city") != null) {
	            predicates.add(
	                cb.equal(root.get("address").get("city"), filters.get("city"))
	            );
	        }
	        
	        if (filters.get("gender") != null) {
	            predicates.add(
	                cb.equal(root.get("gender"), (Gender) filters.get("gender"))
	            );
	        }

	        // ------- FACILITIES FILTER -------
	        if (filters.get("facilities") != null) {

	            List<String> selectedFacilities = (List<String>) filters.get("facilities");

	            for (String field : selectedFacilities) {
	                predicates.add(
	                    cb.isTrue(root.get("facilities").get(field))
	                );
	            }
	        }





	        // ------- ROOM JOIN -------
	        Join<Object, Object> roomJoin = null;
	        if (filters.get("sharingType") != null ||
	            filters.get("minRent") != null ||
	            filters.get("maxRent") != null) 
	        {
	            roomJoin = root.join("roomType", JoinType.LEFT);
	        }

	        // ------- SHARING TYPE FILTER -------
	        if (filters.get("sharingType") != null) {
	            Integer type = Integer.parseInt(filters.get("sharingType").toString());
	            predicates.add(
	                cb.equal(roomJoin.get("sharingType"), type)
	            );

	            query.distinct(true); // duplicate hostel avoid
	        }

	        // ------- RENT FILTER -------
	        if (filters.get("minRent") != null) {
	            predicates.add(
	                cb.greaterThanOrEqualTo(
	                    roomJoin.get("pricePerMonth"),
	                    (Integer) filters.get("minRent")
	                )
	            );
	        }

	        if (filters.get("maxRent") != null) {
	            predicates.add(
	                cb.lessThanOrEqualTo(
	                    roomJoin.get("pricePerMonth"),
	                    (Integer) filters.get("maxRent")
	                )
	            );
	        }

	     // ------- RATING FILTER -------
	        if (filters.get("rating") != null) {

	            Join<Object, Object> ratingJoin = root.join("ratings", JoinType.LEFT);

	            // AVG(score)
	            Expression<Double> avgRating = cb.avg(ratingJoin.get("score"));

	            query.groupBy(root.get("id"));

	            // Add HAVING clause: avg(score) >= rating
	            query.having(
	                cb.greaterThanOrEqualTo(avgRating, (Double) filters.get("rating"))
	            );
	        }


	        return cb.and(predicates.toArray(new Predicate[0]));
	    };
	}

	
}
