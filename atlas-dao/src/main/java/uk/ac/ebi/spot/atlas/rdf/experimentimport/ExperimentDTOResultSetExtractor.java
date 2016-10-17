package uk.ac.ebi.spot.atlas.rdf.experimentimport;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import uk.ac.ebi.spot.rdf.model.ExperimentType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentDTOResultSetExtractor implements ResultSetExtractor<List<AtlasExperimentDTO>> {
    @Override
    public List<AtlasExperimentDTO> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<String, AtlasExperimentDTO> experimentByAccession = Maps.newHashMap();

        while (resultSet.next()) {
            String experimentAccession = resultSet.getString("accession");

            AtlasExperimentDTO experiment = experimentByAccession.get(experimentAccession);

            if (experiment == null) {
                experiment = createExperimentDTO(resultSet, experimentAccession);
                experimentByAccession.put(experimentAccession, experiment);
            }

            String species = resultSet.getString("organism");
            if (!StringUtils.isBlank(species)) {
                experiment.setSpecies(species);
            }

        }

        return Lists.newArrayList(experimentByAccession.values());


    }

    private AtlasExperimentDTO createExperimentDTO(ResultSet resultSet, String experimentAccession) throws SQLException {
        AtlasExperimentDTO experiment;
        ExperimentType experimentType = ExperimentType.valueOf(resultSet.getString("type"));
        Date lastUpdate = resultSet.getTimestamp("last_update");
        boolean isPrivate = "T".equals(resultSet.getString("private"));
        String accessKeyUUID = resultSet.getString("access_key");
        String title = resultSet.getString("title");

        String pubMedIdsString = resultSet.getString("pubmed_Ids");
        Set<String> pubMedIds = StringUtils.isBlank(pubMedIdsString)? new HashSet<String>() : Sets.newHashSet(Splitter.on(", ").split(pubMedIdsString));

        experiment = new AtlasExperimentDTO(experimentAccession
                , experimentType
                , pubMedIds
                , title
                , lastUpdate
                , isPrivate
                , accessKeyUUID);
        return experiment;
    }
}
