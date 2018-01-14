package hello;

public class Path {

    private final long hopCount;
    private final String path;

    public Path(long hopCount, String path) {
        this.hopCount = hopCount;
        this.path = path;
    }

    public long getHopCount() {
        return hopCount;
    }

    public String getPath() {
        return path;
    }
}
