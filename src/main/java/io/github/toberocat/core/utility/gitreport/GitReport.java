package io.github.toberocat.core.utility.gitreport;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Bukkit;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.text.MessageFormat;
import java.util.logging.Level;

public class GitReport {
    private static GitReport gitReport;

    private final GitHubClient client;
    private final IssueService issueService;


    private GitReport() {
        this.client = new GitHubClient().setOAuth2Token("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");
        this.issueService = new IssueService(client);
        gitReport = this;
    }

    public static void reportIssue(Exception e) {
        if (gitReport == null) new GitReport();

        String stackTrace = Utility.printStackToString(e);
        Issue issue = new Issue();
        issue.setTitle("Exception Report: " + e.getMessage());
        String issueBody = """
                **This stacktrace got automatically reported**

                Version: {0}
                Plugin version: {1}
                
                Stacktrace:
                ```java
                {2}
                ```
                """;
        issue.setBody(MessageFormat.format(issueBody, Bukkit.getBukkitVersion(), MainIF.getVersion().getVersion(), stackTrace));

        try {
            gitReport.issueService.createIssue("ToberoCat", "ImprovedFaction", issue);
        } catch (Exception ex) {
            MainIF.logMessage(Level.WARNING, "Unable to report occurred issue to github. PLease send it to the developer if you are connected to the wifi");
            ex.printStackTrace();
        }
    }

}
