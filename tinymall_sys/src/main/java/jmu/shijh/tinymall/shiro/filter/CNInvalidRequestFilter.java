package jmu.shijh.tinymall.shiro.filter;

import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class CNInvalidRequestFilter extends AccessControlFilter {
    private static final List<String> SEMICOLON = Collections.unmodifiableList(Arrays.asList(";", "%3b", "%3B"));
    private static final List<String> BACKSLASH = Collections.unmodifiableList(Arrays.asList("\\", "%5c", "%5C"));
    private boolean blockSemicolon = true;
    private boolean blockBackslash = !Boolean.getBoolean("org.apache.shiro.web.ALLOW_BACKSLASH");
    private boolean blockNonAscii = true;

    protected boolean isAccessAllowed(ServletRequest req, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest request = WebUtils.toHttp(req);
        return this.isValid(request.getRequestURI()) && this.isValid(request.getServletPath()) && this.isValid(request.getPathInfo());
    }

    private boolean isValid(String uri) {
        return !StringUtils.hasText(uri) || !this.containsSemicolon(uri) && !this.containsBackslash(uri) && !this.containsNonAsciiCharacters(uri);
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.toHttp(response).sendError(400, "Invalid request");
        return false;
    }

    private boolean containsSemicolon(String uri) {
        if (this.isBlockSemicolon()) {
            Stream<String> var10000 = SEMICOLON.stream();
            Objects.requireNonNull(uri);
            return var10000.anyMatch(uri::contains);
        } else {
            return false;
        }
    }

    private boolean containsBackslash(String uri) {
        if (this.isBlockBackslash()) {
            Stream<String> var10000 = BACKSLASH.stream();
            Objects.requireNonNull(uri);
            return var10000.anyMatch(uri::contains);
        } else {
            return false;
        }
    }

    private boolean containsNonAsciiCharacters(String uri) {
        if (this.isBlockNonAscii()) {
            return !containsOnlyPrintableAsciiCharacters(uri);
        } else {
            return false;
        }
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    private static boolean containsOnlyPrintableAsciiCharacters(String uri) {
        int length = uri.length();

        for(int i = 0; i < length; ++i) {
            char c = uri.charAt(i);
            if ((c < ' ' || c > '~') && !isChinese(c)) {
                return false;
            }
        }

        return true;
    }

    public boolean isBlockSemicolon() {
        return this.blockSemicolon;
    }

    public void setBlockSemicolon(boolean blockSemicolon) {
        this.blockSemicolon = blockSemicolon;
    }

    public boolean isBlockBackslash() {
        return this.blockBackslash;
    }

    public void setBlockBackslash(boolean blockBackslash) {
        this.blockBackslash = blockBackslash;
    }

    public boolean isBlockNonAscii() {
        return this.blockNonAscii;
    }

    public void setBlockNonAscii(boolean blockNonAscii) {
        this.blockNonAscii = blockNonAscii;
    }
}
