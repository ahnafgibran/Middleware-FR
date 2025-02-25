package id.grit.facereco.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaceSimilarityRes {
    private double similarity;
    private PersonEngineRes personDetail;

    @Override
    public String toString() {
        return "FaceSimilarityRes [similarity=" + similarity + ", personDetail=" + personDetail + "]";
    }
}
