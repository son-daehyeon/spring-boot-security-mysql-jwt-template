const {Octokit} = require("@octokit/rest");
const fs = require("fs");
const path = require("path");

const args = process.argv.slice(2);

if (args.length !== 3) {
    console.error("❌ Usage: npm start <GITHUB_TOKEN> [REPO_OWNER] [REPO_NAME]");
    process.exit(1);
}

const GITHUB_TOKEN = args[0];
const REPO_OWNER = args[1]
const REPO_NAME = args[2]

console.log(`🎯 Target Repository: ${REPO_OWNER}/${REPO_NAME}`);

const octokit = new Octokit({
    auth: GITHUB_TOKEN,
});

const labelsPath = path.join(__dirname, "../.github/labels.json");

if (!fs.existsSync(labelsPath)) {
    console.error("❌ labels.json file not found at:", labelsPath);
    process.exit(1);
}

const desiredLabels = JSON.parse(fs.readFileSync(labelsPath, "utf8"));

async function syncLabels() {
    try {
        console.log("🔄 Syncing labels...");

        try {
            await octokit.rest.users.getAuthenticated();
            console.log("✅ GitHub authentication successful");
        } catch (error) {
            console.error("❌ GitHub authentication failed:", error.message);
            process.exit(1);
        }

        const {data: existingLabels} = await octokit.rest.issues.listLabelsForRepo({
            owner: REPO_OWNER, repo: REPO_NAME,
        });

        const existingLabelMap = new Map();
        existingLabels.forEach(label => {
            existingLabelMap.set(label.name, label);
        });

        let created = 0;
        let updated = 0;
        let deleted = 0;

        console.log(`📋 Found ${existingLabels.length} existing labels`);

        for (const desiredLabel of desiredLabels) {
            const existingLabel = existingLabelMap.get(desiredLabel.name);

            if (existingLabel) {
                if (existingLabel.color !== desiredLabel.color || existingLabel.description !== desiredLabel.description) {
                    await octokit.rest.issues.updateLabel({
                        owner: REPO_OWNER,
                        repo: REPO_NAME,
                        name: desiredLabel.name,
                        color: desiredLabel.color,
                        description: desiredLabel.description,
                    });
                    console.log(`✏️  Updated label: ${desiredLabel.name}`);
                    updated++;
                } else {
                    console.log(`⚪ No changes needed: ${desiredLabel.name}`);
                }
                existingLabelMap.delete(desiredLabel.name);
            } else {
                await octokit.rest.issues.createLabel({
                    owner: REPO_OWNER,
                    repo: REPO_NAME,
                    name: desiredLabel.name,
                    color: desiredLabel.color,
                    description: desiredLabel.description,
                });
                console.log(`✅ Created label: ${desiredLabel.name}`);
                created++;
            }

            await new Promise(resolve => setTimeout(resolve, 100));
        }

        if (existingLabelMap.size > 0) {
            console.log(`\n🗑️  Deleting ${existingLabelMap.size} unused labels...`);

            for (const [labelName] of existingLabelMap) {
                try {
                    await octokit.rest.issues.deleteLabel({
                        owner: REPO_OWNER, repo: REPO_NAME, name: labelName,
                    });
                    console.log(`🗑️  Deleted label: ${labelName}`);
                    deleted++;

                    await new Promise(resolve => setTimeout(resolve, 150));
                } catch (error) {
                    console.warn(`⚠️  Failed to delete label '${labelName}': ${error.message}`);
                }
            }
        }

        console.log("\n📊 Sync completed:");
        console.log(`   ✅ Created: ${created}`);
        console.log(`   ✏️  Updated: ${updated}`);
        console.log(`   🗑️  Deleted: ${deleted}`);
        console.log(`   📦 Total configured labels: ${desiredLabels.length}`);

    } catch (error) {
        console.error("❌ Error syncing labels:", error.message);

        if (error.status === 404) {
            console.error("💡 Make sure the repository exists and your token has proper permissions");
        } else if (error.status === 401) {
            console.error("💡 Check your GitHub token permissions");
        } else if (error.status === 403) {
            console.error("💡 Rate limit exceeded or insufficient permissions");
        }

        process.exit(1);
    }
}

syncLabels();