import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { ThemeProvider } from "next-themes";
import { SidebarProvider } from "@/components/ui/sidebar";
import ReactQueryProvider from "@/providers/react-query";
import { Toaster } from "@/components/ui/sonner";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: {
    default: "Bookmark Manager – Save & Organize Your Links",
    template: "%s | Bookmark Manager",
  },
  description:
    "A modern bookmark manager to save, organize, and tag your favorite links securely.",
  keywords: [
    "bookmark manager",
    "save links",
    "organize bookmarks",
    "web bookmarks",
    "link organizer",
  ],
  verification: {
    google: "YGWDQYuUNOVbmAuGEyq0nA86c-4pal3B6CUKSqZKV-Y",
  },
  metadataBase: new URL("https://bookmark-manager-xyz.vercel.app"),
  openGraph: {
    title: "Bookmark Manager",
    description: "Save, organize, and access your bookmarks anywhere.",
    url: "https://bookmark-manager-xyz.vercel.app",
    siteName: "Bookmark Manager",
    images: [
      {
        url: "/logo.png",
        width: 1200,
        height: 630,
        alt: "Bookmark Manager",
      },
    ],
    type: "website",
  },
  twitter: {
    card: "summary_large_image",
    title: "Bookmark Manager",
    description: "Save and organize bookmarks easily.",
    images: ["/logo.png"],
  },
  robots: {
    index: true,
    follow: true,
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <SidebarProvider>
          <ReactQueryProvider>
            <ThemeProvider attribute="class" defaultTheme="light" enableSystem>
              <div className="flex w-full">
                <main className="flex-1 flex flex-col overflow-hidden w-full">
                  {children}
                  <Toaster />
                </main>
              </div>
            </ThemeProvider>
          </ReactQueryProvider>
        </SidebarProvider>
      </body>
    </html>
  );
}
