package io.github.holmofy.spider.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GithubResp {

    @JSONField(name = "current_user_url")
    private String currentUserUrl;
    @JSONField(name = "current_user_authorizations_html_url")
    private String currentUserAuthorizationsHtmlUrl;
    @JSONField(name = "authorizations_url")
    private String authorizationsUrl;
    @JSONField(name = "code_search_url")
    private String codeSearchUrl;
    @JSONField(name = "commit_search_url")
    private String commitSearchUrl;
    @JSONField(name = "emails_url")
    private String emailsUrl;
    @JSONField(name = "emojis_url")
    private String emojisUrl;
    @JSONField(name = "events_url")
    private String eventsUrl;
    @JSONField(name = "feeds_url")
    private String feedsUrl;
    @JSONField(name = "followers_url")
    private String followersUrl;
    @JSONField(name = "following_url")
    private String followingUrl;
    @JSONField(name = "gists_url")
    private String gistsUrl;
    @JSONField(name = "hub_url")
    private String hubUrl;
    @JSONField(name = "issue_search_url")
    private String issueSearchUrl;
    @JSONField(name = "issues_url")
    private String issuesUrl;
    @JSONField(name = "keys_url")
    private String keysUrl;
    @JSONField(name = "label_search_url")
    private String labelSearchUrl;
    @JSONField(name = "notifications_url")
    private String notificationsUrl;
    @JSONField(name = "organization_url")
    private String organizationUrl;
    @JSONField(name = "organization_repositories_url")
    private String organizationRepositoriesUrl;
    @JSONField(name = "organization_teams_url")
    private String organizationTeamsUrl;
    @JSONField(name = "public_gists_url")
    private String publicGistsUrl;
    @JSONField(name = "rate_limit_url")
    private String rateLimitUrl;
    @JSONField(name = "repository_url")
    private String repositoryUrl;
    @JSONField(name = "repository_search_url")
    private String repositorySearchUrl;
    @JSONField(name = "current_user_repositories_url")
    private String currentUserRepositoriesUrl;
    @JSONField(name = "starred_url")
    private String starredUrl;
    @JSONField(name = "starred_gists_url")
    private String starredGistsUrl;
    @JSONField(name = "topic_search_url")
    private String topicSearchUrl;
    @JSONField(name = "user_url")
    private String userUrl;
    @JSONField(name = "user_organizations_url")
    private String userOrganizationsUrl;
    @JSONField(name = "user_repositories_url")
    private String userRepositoriesUrl;
    @JSONField(name = "user_search_url")
    private String userSearchUrl;
}
