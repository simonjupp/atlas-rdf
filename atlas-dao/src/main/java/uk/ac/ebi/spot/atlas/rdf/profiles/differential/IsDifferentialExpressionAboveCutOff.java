package uk.ac.ebi.spot.atlas.rdf.profiles.differential;

import com.google.common.base.Predicate;
import uk.ac.ebi.spot.rdf.model.differential.DifferentialExpression;
import uk.ac.ebi.spot.rdf.model.differential.Regulation;

/**
 * @author Simon Jupp
 * @date 07/08/2014
 * Samples, Phenotypes and Ontologies Team, EMBL-EBI
 */
public class IsDifferentialExpressionAboveCutOff implements Predicate<DifferentialExpression> {

    private Regulation regulation;
    private double pValueCutOff;
    private double foldChangeCutOff;

    @Override
    public boolean apply(DifferentialExpression differentialExpression) {

        if (Regulation.UP == regulation){
            return isOverExpressed(differentialExpression);
        }
        if (Regulation.DOWN == regulation){
            return isUnderExpressed(differentialExpression);
        }
        return isUnderExpressed(differentialExpression) || isOverExpressed(differentialExpression);
    }

    private boolean isOverExpressed(DifferentialExpression differentialExpression) {
        return differentialExpression.getPValue() <= pValueCutOff && differentialExpression.isOverExpressed()
                && differentialExpression.getAbsoluteFoldChange() >= foldChangeCutOff;
    }

    private boolean isUnderExpressed(DifferentialExpression differentialExpression){
        return differentialExpression.getPValue() <= pValueCutOff && differentialExpression.isUnderExpressed()
                && differentialExpression.getAbsoluteFoldChange() >= foldChangeCutOff;
    }


    public IsDifferentialExpressionAboveCutOff setPValueCutoff(double pValueCutOff){
        this.pValueCutOff = pValueCutOff;
        return this;
    }

    public IsDifferentialExpressionAboveCutOff setFoldChangeCutOff(double foldChangeCutOff) {
        this.foldChangeCutOff = foldChangeCutOff;
        return this;
    }

    public IsDifferentialExpressionAboveCutOff setRegulation(Regulation regulation){
        this.regulation = regulation;
        return this;
    }

}
