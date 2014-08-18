package uk.ac.ebi.spot.atlas.rdf.profiles.differential.rnaseq;

import uk.ac.ebi.spot.atlas.rdf.profiles.DifferentialProfileStreamOptions;
import uk.ac.ebi.spot.atlas.rdf.profiles.RankProfiles;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.DifferentialProfileComparatorFactory;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.DifferentialProfilesListBuilder;
import uk.ac.ebi.spot.atlas.rdf.profiles.differential.RankProfilesFactory;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialProfilesList;
import uk.ac.ebi.spot.rdf.model.differential.rnaseq.RnaSeqProfile;

import java.util.Comparator;

/**
 * @author Simon Jupp
 * @date 11/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class RankRnaSeqProfilesFactory implements RankProfilesFactory<RnaSeqProfile, DifferentialProfilesList<RnaSeqProfile>, DifferentialProfileStreamOptions> {

    private DifferentialProfileComparatorFactory<RnaSeqProfile> differentialProfileComparatorFactory;
    private DifferentialProfilesListBuilder<RnaSeqProfile> geneProfilesListBuilder;

    public RankRnaSeqProfilesFactory(DifferentialProfileComparatorFactory<RnaSeqProfile> differentialProfileComparatorFactory, DifferentialProfilesListBuilder<RnaSeqProfile> geneProfilesListBuilder) {
        this.differentialProfileComparatorFactory = differentialProfileComparatorFactory;
        this.geneProfilesListBuilder = geneProfilesListBuilder;
    }

    public RankProfiles<RnaSeqProfile, DifferentialProfilesList<RnaSeqProfile>> create(DifferentialProfileStreamOptions options) {
        Comparator<RnaSeqProfile> comparator = differentialProfileComparatorFactory.create(options);
        return new RankProfiles<>(comparator, geneProfilesListBuilder);
    }

}