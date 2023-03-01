package belykh.projects.Timer.out.rest.api.request.wrapper.archivist;

/**
 * Endpoints RestApi - Archivist
 */
public enum ArchivistApiEndPoints {
    SAVE("/save");

    private final String commandPath;

    ArchivistApiEndPoints(String commandPath) {
        this.commandPath = commandPath;
    }

    public String getCommandPath() {
        return commandPath;
    }
}
