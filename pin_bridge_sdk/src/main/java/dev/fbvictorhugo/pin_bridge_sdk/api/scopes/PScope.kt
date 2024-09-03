package dev.fbvictorhugo.pin_bridge_sdk.api.scopes

sealed class PScope(val scope: String, val description: String) {

    companion object {

        fun build(vararg scopes: PScope): String {
            return scopes.joinToString(separator = ",") { it.scope }
        }
    }

    sealed class Pins(scope: String, description: String) : PScope(scope, description) {

        /**
         * See public Pins
         */
        data object Read : Pins("pins:read", "See public Pins")

        /**
         * See your secret Pins
         */
        data object ReadSecret : Pins("pins:read_secret", "See your secret Pins")

        /**
         * Create, update, or delete your public Pins
         */
        data object Write : Pins("pins:write", "Create, update, or delete your public Pins")

        /**
         * Create, update, or delete your secret Pins
         */
        data object WriteSecret :
            Pins("pins:write_secret", "Create, update, or delete your secret Pins")
    }

    sealed class Boards(scope: String, description: String) : PScope(scope, description) {

        /**
         * See public boards, including group boards
         */
        data object Read : Boards("boards:read", "See public boards, including group boards")

        /**
         * See your secret boards
         */
        data object ReadSecret : Boards("boards:read_secret", "See your secret boards")

        /**
         * Create, update, or delete your public boards
         */
        data object Write : Boards("boards:write", "Create, update, or delete your public boards")

        /**
         * Create, update, or delete your secret boards
         */
        data object WriteSecret :
            Boards("boards:write_secret", "Create, update, or delete your secret boards")
    }

    sealed class Ads(scope: String, description: String) : PScope(scope, description) {

        /**
         * See all advertising data, including ads, ad groups, campaigns etc.
         */
        data object Read :
            Ads("ads:read", "See all advertising data, including ads, ad groups, campaigns etc.")

        /**
         * Create, update, or delete ads, ad groups, campaigns etc.
         */
        data object Write :
            Ads("ads:write", "Create, update, or delete ads, ad groups, campaigns etc.")
    }

    sealed class Billing(scope: String, description: String) : PScope(scope, description) {

        /**
         * See all of your billing data, billing profile, etc.
         */
        data object Read :
            Billing("billing:read", "See all of your billing data, billing profile, etc.")

        /**
         * Create, update, or delete billing data, billing profiles, etc.
         */
        data object Write : Billing(
            "billing:write",
            "Create, update, or delete billing data, billing profiles, etc."
        )
    }

    sealed class BizAccess(scope: String, description: String) : PScope(scope, description) {

        /**
         * See business access data
         */
        data object Read : BizAccess("biz_access:read", "See business access data")

        /**
         * Create, update, or delete business access data
         */
        data object Write :
            BizAccess("biz_access:write", "Create, update, or delete business access data")
    }

    sealed class Catalogs(scope: String, description: String) : PScope(scope, description) {

        /**
         * See all catalogs data
         */
        data object Read : Catalogs("catalogs:read", "See all catalogs data")

        /**
         * Create, update, or delete catalogs data
         */
        data object Write : Catalogs("catalogs:write", "Create, update, or delete catalogs data")
    }

    sealed class UserAccounts(scope: String, description: String) : PScope(scope, description) {

        /**
         * See your user accounts and followers
         */
        data object Read :
            UserAccounts("user_accounts:read", "See your user accounts and followers")

        /**
         * Update your user accounts and followers
         */
        data object Write :
            UserAccounts("user_accounts:write", "Update your user accounts and followers")
    }
}
