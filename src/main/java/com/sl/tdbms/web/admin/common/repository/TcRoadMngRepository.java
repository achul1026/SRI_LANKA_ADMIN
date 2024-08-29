package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TcRoadMng;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TcRoadMngRepository extends JpaRepository<TcRoadMng, String>{

    @Query(value =
            "select "+
                    "TRM.ROAD_CD as roadCd , "+
                    "TRM.ROAD_DESCR as roadDescr , "+
                    "ST_ASGEOJSON(TRM.GEOM) as geom "+
            "from "+
                    "srlk.TC_ROAD_MNG TRM "
            ,
            nativeQuery = true)
    List<Map<String,Object>> getRoadList();

    /*@Query(value =
            "select "+
                    "ROUND(cast(SUM(" +
                    "        CASE " +
                    "            WHEN ST_LineLocatePoint(line_geom, start_geom_point) < ST_LineLocatePoint(line_geom, survey_geom_point) THEN " +
                    "                ST_Length(" +
                    "                    ST_GeographyFromText(" +
                    "                        ST_AsText(" +
                    "                            ST_LineSubstring(" +
                    "                                line_geom," +
                    "                                ST_LineLocatePoint(line_geom, start_geom_point)," +
                    "                                ST_LineLocatePoint(line_geom, survey_geom_point)" +
                    "                            )" +
                    "                        )" +
                    "                    )" +
                    "                )" +
                    "            ELSE" +
                    "                0" +
                    "        END" +
                    "    ) as numeric),0) AS distance "+
            "from ( "+
                    "select road_cd, road_descr, roadstart_lat, roadstart_lon "+
                    ", st_pointz(roadstart_lon, roadstart_lat, 0, 4326) as start_point  "+
                    ", st_3dclosestpoint(geom, st_pointz(roadstart_lon, roadstart_lat, 0, 4326)) as start_geom_point  "+
                    ", st_3dclosestpoint(geom, st_pointz(:lon, :lat, 0, 4326)) as survey_geom_point  "+
                    ", geom "+
                    ", (st_dump(geom)).geom as line_geom "+
                    "from srlk.tc_road_mng "+
                    "where road_cd = :roadCd "+
            ") a "+
            "group by road_cd, road_descr, start_point, start_geom_point "
            ,
            nativeQuery = true)*/
    @Query(value = "WITH point_location AS ( " +
            "    select " +
            "    road_cd, " +
            "ST_LineLocatePoint(ST_LineMerge(ST_SnapToGrid(geom, 0.001))," +
            "        ST_SetSRID(ST_MakePoint(:lon, :lat)," +
            "        4326)) AS location" +
            "    FROM " +
            "        srlk.tc_road_mng " +
            "    WHERE " +
            "        road_cd = :roadCd " +
            ") " +
            " " +
            "SELECT " +
            "    ROUND(cast(sum(ST_Length( ST_Transform(ST_LineSubstring(ST_LineMerge(ST_SnapToGrid(geom, 0.001)),0,pl.location),3857))) as numeric),0) AS distance " +
            "FROM srlk.tc_road_mng l " +
            "JOIN point_location pl ON l.road_cd = pl.road_cd group by l.road_cd",
            nativeQuery = true)
    BigDecimal getLocationDistance(@Param("roadCd") String roadCd,@Param("lon") BigDecimal lon,
                               @Param("lat") BigDecimal lat);

    @Query(value="SELECT trm.road_cd AS \"roadCd\" "
    		+ "	FROM srlk.tc_road_mng trm "
    		+ "", nativeQuery = true)
	List<String> getRoadCdList();

}
