package io.github.holmofy.spider.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GithubResp {

    @SerializedName("current_user_url")
    private String currentUserUrl;
    @SerializedName("current_user_authorizations_html_url")
    private String currentUserAuthorizationsHtmlUrl;
    @SerializedName("authorizations_url")
    private String authorizationsUrl;
    @SerializedName("code_search_url")
    private String codeSearchUrl;
    @SerializedName("commit_search_url")
    private String commitSearchUrl;
    @SerializedName("emails_url")
    private String emailsUrl;
    @SerializedName("emojis_url")
    private String emojisUrl;
    @SerializedName("events_url")
    private String eventsUrl;
    @SerializedName("feeds_url")
    private String feedsUrl;
    @SerializedName("followers_url")
    private String followersUrl;
    @SerializedName("following_url")
    private String followingUrl;
    @SerializedName("gists_url")
    private String gistsUrl;
    @SerializedName("hub_url")
    private String hubUrl;
    @SerializedName("issue_search_url")
    private String issueSearchUrl;
    @SerializedName("issues_url")
    private String issuesUrl;
    @SerializedName("keys_url")
    private String keysUrl;
    @SerializedName("label_search_url")
    private String labelSearchUrl;
    @SerializedName("notifications_url")
    private String notificationsUrl;
    @SerializedName("organization_url")
    private String organizationUrl;
    @SerializedName("organization_repositories_url")
    private String organizationRepositoriesUrl;
    @SerializedName("organization_teams_url")
    private String organizationTeamsUrl;
    @SerializedName("public_gists_url")
    private String publicGistsUrl;
    @SerializedName("rate_limit_url")
    private String rateLimitUrl;
    @SerializedName("repository_url")
    private String repositoryUrl;
    @SerializedName("repository_search_url")
    private String repositorySearchUrl;
    @SerializedName("current_user_repositories_url")
    private String currentUserRepositoriesUrl;
    @SerializedName("starred_url")
    private String starredUrl;
    @SerializedName("starred_gists_url")
    private String starredGistsUrl;
    @SerializedName("topic_search_url")
    private String topicSearchUrl;
    @SerializedName("user_url")
    private String userUrl;
    @SerializedName("user_organizations_url")
    private String userOrganizationsUrl;
    @SerializedName("user_repositories_url")
    private String userRepositoriesUrl;
    @SerializedName("user_search_url")
    private String userSearchUrl;
}
