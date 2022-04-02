package xml;
import java.util.ArrayList;
import java.util.List;

public class ResultSet {
    public List<Result> results;

    public ResultSet(List<Result> results) {
        this.results = results;
    }

    public ResultSet() {
        this.results = new ArrayList<>();
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void appendResult(Result result) {
        this.results.add(result);
    }
}
