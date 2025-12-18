import { MetadataRoute } from "next";

export default function sitemap(): MetadataRoute.Sitemap {
  return [
    {
      url: "https://bookmark-manager-xyz.vercel.app",
      lastModified: new Date(),
      changeFrequency: "weekly",
      priority: 1,
    },
    {
      url: "https://bookmark-manager-xyz.vercel.app/sign-in",
      priority: 0.2,
    },
    {
      url: "https://bookmark-manager-xyz.vercel.app/sign-up",
      priority: 0.2,
    },
  ];
}
