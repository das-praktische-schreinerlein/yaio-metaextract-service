package de.yaio.services.metaextract.server.extractor;

public class ExtractorException extends Exception {
    protected Object src = null;
    public ExtractorException(final String message, final Object src, final Exception cause) {
        super(message, cause);
        this.src = src;
    }

    public Object getSrc() {
        return src;
    }
}
