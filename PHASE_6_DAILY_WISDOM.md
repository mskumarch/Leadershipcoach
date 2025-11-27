# Phase 6: Daily Wisdom & Continuous Growth

## Objective
Implement a "Daily Wisdom" page to drive daily engagement and continuous learning. This feature aligns perfectly with the Master Coach persona's "Loop of Growth" principle, providing value even when the user isn't actively recording a session.

## 1. The "Daily Wisdom" Screen
- [x] **Design**: A beautiful, magazine-style layout using the existing Glassmorphism system.
- [x] **Hero Section ("Tip of the Day")**:
    - A large, visually striking card.
    - **Content**: A single, actionable leadership tip derived from a top blog post.
    - **AI Magic**: Use Gemini to synthesize the tip from the RSS feed content (e.g., "Based on HBR's latest article: 'Stop solving problems for your team; ask them for 3 solutions instead.'").
- [x] **Feed Stream**:
    - A clean list of recent articles from curated sources (e.g., HBR, First Round Review, Simon Sinek).
    - **Quick Actions**: "Read Summary" (AI generated) or "Open Link".

## 2. Technical Architecture
- [x] **RSS Engine**: Implement a lightweight `RssRepository` using `XmlPullParser` (standard Android) to fetch feeds from URLs.
- [x] **AI Summarization**:
    - **Trigger**: When a new article is fetched.
    - **Prompt**: "Summarize this leadership article into one 'Golden Nugget' tip and 3 key takeaways."
- [ ] **Caching**: Store the "Tip of the Day" locally (Room DB or DataStore) so it doesn't regenerate every time the user opens the app (cost & performance optimization).

## 3. Navigation Integration
- [x] **New Tab**: Add a "Wisdom" tab (icon: 💡) to the `FloatingPillNav`.
- [x] **Home Widget**: Add a small "Daily Tip" teaser card to the Home screen (`ChatScreen` idle state) that links to the full Wisdom screen.

## 4. Social Sharing
- [x] **Share Intent**: A "Share Wisdom" button that formats the tip as a nice text block for LinkedIn or Slack.
    - *Format*: "💡 Daily Leadership Tip: [Tip] (Source: [Article]) #Leadership #Growth"

## 5. Curated Sources (Initial List)
- **Harvard Business Review**: `http://feeds.hbr.org/harvardbusiness` (General Leadership)
- **First Round Review**: `https://review.firstround.com/rss` (Tech/Startup Leadership)
- **Leadership Freak**: `https://leadershipfreak.blog/feed` (Daily, actionable tips)
- **McKinsey Insights**: `https://www.mckinsey.com/featured-insights/rss.xml` (Strategic Leadership)

## 6. Progress
- [x] **Daily Wisdom**: Implemented RSS Engine, Wisdom Screen, and AI Summarization.
- [x] **Refinements**: Added Social Sharing, Home Screen Teaser, Manual Refresh, and Full Screen Detail View.
- [ ] **Immersive Practice**: Pending.
- [ ] **Personalization**: Pending.
